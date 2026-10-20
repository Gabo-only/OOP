/**
    this is created when a returned item is found damaged
    this is a association linked to the BorrowRequest it came from
 */
public class DamageReport {

    private String reportId;
    private BorrowRequest sourceRequest;
    private String description;
    private String severity; // example "MINOR", "MAJOR"

    public DamageReport(String reportId, BorrowRequest sourceRequest, String description, String severity) {
        this.reportId = reportId;
        this.sourceRequest = sourceRequest;
        this.description = description;
        this.severity = severity;
    }

    public String getReportId() { return reportId; }
    public BorrowRequest getSourceRequest() { return sourceRequest; }
    public String getDescription() { return description; }
    public String getSeverity() { return severity; }

    @Override
    public String toString() {
        return String.format("DamageReport[%s] on %s - %s (%s)",
                reportId, sourceRequest.getRequestId(), description, severity);
    }
}
