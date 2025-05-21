package io.beanvortex.bitkip.config.observers;

import io.beanvortex.bitkip.config.AppConfigs;
import javafx.scene.Scene;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ThemeSubjectTest {

    @Mock
    private ThemeObserver observer1;
    
    @Mock
    private ThemeObserver observer2;
    
    @Mock
    private Scene scene1;
    
    @Mock
    private Scene scene2;
    
    private String originalTheme;
    
    @BeforeEach
    void setUp() {
        originalTheme = AppConfigs.theme;
    }
    
    @Test
    void testAddObserver() {
        // Get the singleton instance
        ThemeSubject subject = ThemeSubject.getThemeSubject();
        
        // Add observer
        subject.addObserver(observer1, scene1);
        
        // Change theme to trigger notification
        ThemeSubject.setTheme("dark");
        
        // Verify observer was notified
        verify(observer1, times(1)).updateTheme(scene1);
        
        // Reset theme
        AppConfigs.theme = originalTheme;
    }
    
    @Test
    void testRemoveObserver() {
        // Get the singleton instance
        ThemeSubject subject = ThemeSubject.getThemeSubject();
        
        // Add observer
        subject.addObserver(observer1, scene1);
        
        // Remove observer
        subject.removeObserver(observer1);
        
        // Change theme to trigger notification
        ThemeSubject.setTheme("dark");
        
        // Verify observer was NOT notified
        verify(observer1, never()).updateTheme(any());
        
        // Reset theme
        AppConfigs.theme = originalTheme;
    }
    
    @Test
    void testSetTheme() {
        // Get the singleton instance
        ThemeSubject subject = ThemeSubject.getThemeSubject();
        
        // Add observers
        subject.addObserver(observer1, scene1);
        subject.addObserver(observer2, scene2);
        
        // Change theme
        ThemeSubject.setTheme("dark");
        
        // Verify theme was changed
        assertEquals("dark", AppConfigs.theme, "Theme should be changed to dark");
        
        // Verify observers were notified
        verify(observer1, times(1)).updateTheme(scene1);
        verify(observer2, times(1)).updateTheme(scene2);
        
        // Reset theme
        AppConfigs.theme = originalTheme;
    }
    
    @Test
    void testMultipleThemeChanges() {
        // Get the singleton instance
        ThemeSubject subject = ThemeSubject.getThemeSubject();
        
        // Add observer
        subject.addObserver(observer1, scene1);
        
        // Change theme multiple times
        ThemeSubject.setTheme("dark");
        ThemeSubject.setTheme("light");
        ThemeSubject.setTheme("dark");
        
        // Verify observer was notified for each change
        verify(observer1, times(3)).updateTheme(scene1);
        
        // Reset theme
        AppConfigs.theme = originalTheme;
    }
}
