package ru.mtuci.antivirus.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "licenses")
public class License {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String code;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "type_id")
    @JsonBackReference
    private LicenseType type;

    @Column(name = "first_activation_date")
    private Date firstActivationDate;

    @Column(name = "ending_date")
    private Date endingDate;

    @Column(name = "is_blocked")
    private Boolean isBlocked;

    @Column(name = "devices_count")
    private int devicesCount;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id")
    @JsonBackReference
    private User owner;

    @Column(name = "duration")
    private int duration;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "license", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeviceLicense> deviceLicenses;

    @OneToMany(mappedBy = "license", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<LicenseHistory> licenseHistories;

    // Конструктор без ID для новых лицензий
    public License(String code, User user, Product product, LicenseType type, Date firstActivationDate, Date endingDate, Boolean isBlocked, int devicesCount, User owner, int duration, String description) {
        this.code = code;
        this.user = user;
        this.product = product;
        this.type = type;
        this.firstActivationDate = firstActivationDate;
        this.endingDate = endingDate;
        this.isBlocked = isBlocked;
        this.devicesCount = devicesCount;
        this.owner = owner;
        this.duration = duration;
        this.description = description;
    }

    // Пустой конструктор для JPA
    public License() {
    }

    // Метод для получения информации о лицензии в строковом виде
    public String getBody() {
        return String.format("License:\n" +
                        "Code: %s\n" +
                        "User: %s\n" +
                        "Product: %s\n" +
                        "Type: %s\n" +
                        "First activation date: %s\n" +
                        "Ending date: %s\n" +
                        "Is blocked: %b\n" +
                        "Devices count: %d\n" +
                        "Owner: %s\n" +
                        "Duration: %d\n" +
                        "Description: %s\n",
                this.getCode(),
                this.getUser().getLogin(),
                this.getProduct().getName(),
                this.getType().getName(),
                this.getFirstActivationDate(),
                this.getEndingDate(),
                this.getIsBlocked(),
                this.getDevicesCount(),
                this.getOwner().getLogin(),
                this.getDuration(),
                this.getDescription());
    }

    // Переопределение toString() для упрощенного представления
    @Override
    public String toString() {
        return "License{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", user=" + user +
                ", product=" + product +
                ", type=" + type +
                ", firstActivationDate=" + firstActivationDate +
                ", endingDate=" + endingDate +
                ", isBlocked=" + isBlocked +
                ", devicesCount=" + devicesCount +
                ", owner=" + owner +
                ", duration=" + duration +
                ", description='" + description + '\'' +
                '}';
    }

    // Метод для генерации подписи (если требуется)
    public String getSignature() {
        // Реализуйте логику для генерации подписи, если она требуется
        return "SomeSignature"; // Пример, можно использовать алгоритм для создания подписи
    }
}

