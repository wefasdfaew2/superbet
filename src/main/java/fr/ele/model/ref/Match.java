package fr.ele.model.ref;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Proxy;

import com.mysema.query.annotations.QueryInit;

import fr.ele.model.HasCodeEntity;
import fr.ele.model.SuperBetTables;
import fr.herman.metatype.annotation.MetaBean;

@MetaBean
@Entity
@Table(name = SuperBetTables.MatchTable.TABLE, uniqueConstraints = @UniqueConstraint(columnNames = {
        SuperBetTables.MatchTable.SPORT_COLUMN,
        SuperBetTables.MatchTable.DATE_COLUMN, SuperBetTables.CODE_COLUMN}))
@Proxy(proxyClass = Match.class)
public class Match extends HasCodeEntity {

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Sport.class)
    @JoinColumn(name = SuperBetTables.MatchTable.SPORT_COLUMN, nullable = false)
    @QueryInit("*")
    private Sport sport;

    @Column(name = SuperBetTables.MatchTable.DATE_COLUMN, nullable = false)
    private Date date;

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
