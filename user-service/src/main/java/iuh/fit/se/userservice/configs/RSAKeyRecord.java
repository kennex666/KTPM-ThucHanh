package iuh.fit.se.userservice.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "rsa")
// Record giúp đơn giản hóa việc tạo các lớp dữ liệu không thể thay đổi (immutable data classes).
// Chúng là một loại lớp tự động tạo ra các phương thức phổ biến như constructor, equals(), hashCode() và toString()
// class Record không thể được mở rộng (extend) hoặc mở rộng một lớp khác. Record được thiết kế để đơn giản hóa việc tạo ra các lớp dữ liệu không thể thay đổi (immutable data classes) và chúng không hỗ trợ tính năng kế thừa
public record RSAKeyRecord(RSAPublicKey rsaPublicKey, RSAPrivateKey rsaPrivateKey) { }
