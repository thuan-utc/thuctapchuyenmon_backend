package cntt2.k61.backend.domain;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Bill bill;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    public Payment(Customer customer, Bill bill, Long amount, String transactionId, Instant createdDate) {
        this.customer = customer;
        this.bill = bill;
        this.amount = amount;
        this.transactionId = transactionId;
        this.createdDate = createdDate;
    }

    public Payment() {
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}

