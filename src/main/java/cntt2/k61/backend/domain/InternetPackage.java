package cntt2.k61.backend.domain;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "packages")
public class InternetPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "package_name", length = 50, nullable = false)
    private String packageName;

    @Column(name = "speed", length = 20, nullable = false)
    private String speed;

    @Column(name = "data_limit", length = 20, nullable = false)
    private String dataLimit;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "created_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Instant createdDate;

    @Column(name = "isdeleted", nullable = false)
    private boolean isDeleted;

    public InternetPackage() {
    }

    public InternetPackage(Long id, String packageName, String speed, String dataLimit, Long price, Instant createdDate, boolean isDeleted) {
        this.id = id;
        this.packageName = packageName;
        this.speed = speed;
        this.dataLimit = dataLimit;
        this.price = price;
        this.createdDate = createdDate;
        this.isDeleted = isDeleted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getDataLimit() {
        return dataLimit;
    }

    public void setDataLimit(String dataLimit) {
        this.dataLimit = dataLimit;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}

