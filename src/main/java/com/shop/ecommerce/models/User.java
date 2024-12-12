package com.shop.ecommerce.models;

import com.shop.ecommerce.models.enums.Gender;
import com.shop.ecommerce.models.enums.Role;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Document(collection = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User  implements UserDetails {
    @Id
    private String username;
    private String name;
    private String email;
    private String password;
    private String image;
    private Gender gender;
    private String phoneNumber;
    private LocalDate dob ;


    @Builder.Default
    private List<Address> addresses = new ArrayList<>();

    @Builder.Default
    @Field(name = "wallet")
    private int wallet = 0;

    @Field(name = "role")
    private Role role; ;

    @Field(name = "auth_provider")
    private String authProvider;

    @Builder.Default
    @Field(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    @Field(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();


    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getRoleName()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}



////////////////////////////////////////////////////////////////////////
//package com.shop.ecommerce.models;
//
//import com.shop.ecommerce.models.enums.AuthProvider;
//import com.shop.ecommerce.models.enums.Gender;
//import com.shop.ecommerce.models.enums.Role;
//import lombok.*;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.index.Indexed;
//import org.springframework.data.mongodb.core.mapping.Document;
//import org.springframework.data.mongodb.core.mapping.Field;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//@Document(collection = "users")
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class User  implements UserDetails {
//
//    @Id
//    private String id;
//
//    @Field(name = "email")
//    @Indexed(unique = true)
//    private String email;
//
//    @Field(name = "phone_number")
//    private String phoneNumber;
//
//    @Field(name = "username")
//    @Indexed(unique = true)
//    private String username;
//
//    @Field(name = "name")
//    private String name;
//
//    @Field(name = "auth_provider")
//    private AuthProvider authProvider;
//
//    @Field(name = "facebook_id")
//    private String facebookId;
//
//    @Field(name = "google_id")
//    private String googleId;
//
//    @Field(name = "password")
//    private String password;
//
//    private String image;
//
//    @Field(name = "gender")
//    private Gender gender;
//
//    @Field(name = "dob")
//    private LocalDate dob ;
//
//    @Builder.Default
//    private List<Address> addresses = new ArrayList<>();
//
//    @Builder.Default
//    @Field(name = "wallet")
//    private int wallet = 0;
//
//    @Field(name = "role")
//    private Role role;
//
//    @Builder.Default
//    @Field(name = "created_at")
//    private LocalDateTime createdAt = LocalDateTime.now();
//
//    @Builder.Default
//    @Field(name = "updated_at")
//    private LocalDateTime updatedAt = LocalDateTime.now();
//
//
//    @Override
//    public String getUsername() {
//        return username;
//    }
//
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of(new SimpleGrantedAuthority(role.getRoleName()));
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return UserDetails.super.isAccountNonExpired();
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return UserDetails.super.isAccountNonLocked();
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return UserDetails.super.isCredentialsNonExpired();
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return UserDetails.super.isEnabled();
//    }
//}
