/* (C)2024 */
package org.example.state;

import java.io.File;
import java.time.LocalDate;

public class PrintInfoModel {

    private final PrintInfoModelPublisher events = new PrintInfoModelPublisher();

    private File file;
    private LocalDate date;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
        this.notifyStateChange();
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
        this.notifyStateChange();
    }

    public PrintInfoModelPublisher getEvents() {
        return this.events;
    }

    private void notifyStateChange() {
        this.events.notifySubscribers(this);
    }
}
