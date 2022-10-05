package com.gamesexchange.gamesexchange.model;

public class Report {
    private String reportId,reporterName,reportedText,reportedTitle,reporterId;

    public Report()
    {}

    public Report(String reportId, String reporterName, String reportedText, String reportedTitle, String reporterId) {
        this.reportId = reportId;
        this.reporterName = reporterName;
        this.reportedText = reportedText;
        this.reportedTitle = reportedTitle;
        this.reporterId = reporterId;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getReportedText() {
        return reportedText;
    }

    public void setReportedText(String reportedText) {
        this.reportedText = reportedText;
    }

    public String getReportedTitle() {
        return reportedTitle;
    }

    public void setReportedTitle(String reportedTitle) {
        this.reportedTitle = reportedTitle;
    }

    public String getReporterId() {
        return reporterId;
    }

    public void setReporterId(String reporterId) {
        this.reporterId = reporterId;
    }
}
