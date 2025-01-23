import java.time.LocalDate;

public class BorrowedBook {

    public int loanNr;
    public int ISBN;
    public int borrowedBy;
    public LocalDate start;
    public LocalDate end;

    public BorrowedBook(int loanNr, int ISBN, int borrowedBy, LocalDate start, LocalDate end) {
        this.loanNr = loanNr;
        this.ISBN = ISBN;
        this.borrowedBy = borrowedBy;
        this.start = start;
        this.end = end;
    }

    public int getLoanNr() {
        return loanNr;
    }

    public void setLoanNr(int loanNr) {
        this.loanNr = loanNr;
    }

    public int getISBN() {
        return ISBN;
    }

    public void setISBN(int ISBN) {
        this.ISBN = ISBN;
    }

    public int getBorrowedBy() {
        return borrowedBy;
    }

    public void setBorrowedBy(int borrowedBy) {
        this.borrowedBy = borrowedBy;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }
}
