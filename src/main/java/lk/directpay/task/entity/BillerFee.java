package lk.directpay.task.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class BillerFee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String billerId;
    private String fee;

    /**
     * @return int return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return String return the billerId
     */
    public String getBillerId() {
        return billerId;
    }

    /**
     * @param billerId the billerId to set
     */
    public void setBillerId(String billerId) {
        this.billerId = billerId;
    }

    /**
     * @return String return the fee
     */
    public String getFee() {
        return fee;
    }

    /**
     * @param fee the fee to set
     */
    public void setFee(String fee) {
        this.fee = fee;
    }

}
