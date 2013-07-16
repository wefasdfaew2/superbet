package fr.ele.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import fr.ele.model.Bet;
import fr.ele.model.SuperBetTables;
import fr.ele.model.ref.BookMaker;
import fr.ele.model.ref.RefKey;

@Entity
@Table(name = SuperBetTables.BetTable.TABLE)
public class Bet extends SuperBetEntity {

    @Column(name = SuperBetTables.BetTable.ODD_COLUMN, nullable = false)
    private double odd;

    @Column(name = SuperBetTables.BetTable.DATE, nullable = false)
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = BookMaker.class)
    @JoinColumn(name = SuperBetTables.BetTable.BOOKMAKER_ID_COLUMN, nullable = false)
    private BookMaker bookMaker;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = RefKey.class)
    @JoinColumn(name = SuperBetTables.BetTable.REFKEY_ID_COLUMN, nullable = false)
    private RefKey refKey;

    public RefKey getRefKey() {
        return refKey;
    }

    public void setRefKey(RefKey refKey) {
        this.refKey = refKey;
    }

    public double getOdd() {
        return odd;
    }

    public void setOdd(double odd) {
        this.odd = odd;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BookMaker getBookMaker() {
        return bookMaker;
    }

    public void setBookMaker(BookMaker bookMaker) {
        this.bookMaker = bookMaker;
    }

}
