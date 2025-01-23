'use strict';

let port = 9563;
let enabled = true;


const updatePort = () => {
    chrome.storage.sync.get(["port"])
        .then((result) => {
            if (isObjectEmpty(result) || result === undefined) chrome.storage.sync.set({ port });
            else port = result.port;
        });
}

const updateEnable = async () => {
    await chrome.storage.local.get(["enabled"])
        .then(res => {
            if (isObjectEmpty(res) || res === undefined) chrome.storage.local.set({ enabled });
            else enabled = res.enabled;
        });
}

updatePort()
updateEnable()
const postLinks = async (data, isBatch) => {
    await updatePort()
    let URL_TO_POST = `http://localhost:${port}/single`;
    if (isBatch)
        URL_TO_POST = `http://localhost:${port}/batch`;
    fetch(URL_TO_POST, {
        method: 'POST',
        body: JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json', // Only necessary headers
        }
    }).catch(async _ => {
        chrome.notifications.create('', {
            title: 'BitKip Extension',
            message: "Can't send url to BitKip, Is application running on the same port?",
            iconUrl: '../resources/icons/logo.png',
            type: 'basic'
        });
        chrome.downloads.onDeterminingFilename.removeListener(triggerDownload)
        await chrome.downloads.download({ url: data.url })
        chrome.downloads.onDeterminingFilename.addListener(triggerDownload)
    });
}


const loadedContentScripts = {};
chrome.runtime.onMessage.addListener(async (message, sender, sendResponse) => {
    switch (message.type) {
        case "captcha":
            const tabId = sender.tab.id;
            loadedContentScripts[tabId] = false;
            sendResponse({ isOkToAddListener: true });
            break;
        case "storeFirstUrl":
            const tabId2 = sender.tab.id;
            if (!loadedContentScripts[tabId2]) {
                sendResponse({ isOkToAddListener: true });
                loadedContentScripts[tabId2] = true;
            } else sendResponse({ isOkToAddListener: false });
            break;
        case "downloadBatch":
            if (message.urls !== 0) {
                const links = message.urls;
                postLinks({ links }, true)
            }
            break;
        case "setPort":
            chrome.storage.sync.set({ port: message.value });
            break;
    }
});


// Prevent the download from starting
// get download link from Chrome Api
const triggerDownload = (downloadItem, suggest) => {
    // final url is used when url itself is a redirecting link
    updateEnable();
    if (!enabled || (downloadItem.mime && downloadItem.mime.includes("image")))
        return;
    let url = downloadItem.finalUrl || downloadItem.url;
    if (isSupportedProtocol(url)) {
        suggest({ cancel: true, filename: downloadItem.filename });
        chrome.downloads.cancel(downloadItem.id, () =>
            chrome.downloads.erase({ id: downloadItem.id })
        );
        let data = {
            url,
            filename: downloadItem.filename,
            fileSize: downloadItem.fileSize,
            agent: navigator.userAgent
        };
        postLinks(data, false);
    }
}

//Main code to maintain download link and start doing job
chrome.downloads.onDeterminingFilename.addListener(triggerDownload);

//Add BitKip right-click menu listener to browser page
chrome.contextMenus.onClicked.addListener((info) => {
    if (info.menuItemId === "extract_selected_link") {
        if (isSupportedProtocol(info.linkUrl)) {
            let data = {
                url: info.linkUrl,
                agent: navigator.userAgent
            };
            postLinks(data, false);
        }
    }
});

//Adding menus to right-click
chrome.contextMenus.removeAll(() => {
    chrome.contextMenus.create({
        id: 'extract_selected_link',
        title: 'Download this link',
        contexts: ['all'],
    });
});


//check link to be valid or not
const isSupportedProtocol = (url) => {
    if (!url) return false;
    let u = new URL(url);
    return u.protocol === 'http:' || u.protocol === 'https:';
}


const isObjectEmpty = (objectName) => {
    return JSON.stringify(objectName) === "{}";
};

let isScriptInjected = false;


chrome.action.onClicked.addListener(async (tab) => {
    if (!isScriptInjected) {
        chrome.scripting.executeScript({
            target: { tabId: tab.id },
            files: ['scripts/uSelect_statemachine.js']
        }).then(() => {
            return chrome.scripting.executeScript({
                target: { tabId: tab.id },
                files: ['scripts/uSelect_overlay.js'],
            });
        }).then(async () => {
            const tabs = await chrome.tabs.query({ active: true, currentWindow: true, lastFocusedWindow: true });
            chrome.tabs.sendMessage(tabs[0].id, { type: 'toggleOverlay' });
        }).catch((error) => {
            console.error('Failed to inject scripts:', error);
        });
        isScriptInjected = true;
    } else {
        const tabs = await chrome.tabs.query({ active: true, currentWindow: true, lastFocusedWindow: true });
        chrome.tabs.sendMessage(tabs[0].id, { type: 'toggleOverlay' });
    }
});

