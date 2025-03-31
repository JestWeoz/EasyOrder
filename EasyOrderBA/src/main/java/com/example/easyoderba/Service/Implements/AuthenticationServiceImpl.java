package com.example.easyoderba.Service.Implements;

import com.example.easyoderba.Exception.AppException;
import com.example.easyoderba.Exception.ErrorCode;
import com.example.easyoderba.Model.DTO.request.AuthReq.IntrospectReq;
import com.example.easyoderba.Model.DTO.request.AuthReq.LoginReq;
import com.example.easyoderba.Model.DTO.response.AuthenticationResponse;
import com.example.easyoderba.Model.DTO.response.IntrospectResponse;
import com.example.easyoderba.Model.Entity.AuthEntity.User;
import com.example.easyoderba.Repository.UserRepository;
import com.example.easyoderba.Service.AuthenticationService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    @NonFinal
    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    @Override
    public AuthenticationResponse Login(LoginReq loginReq) {
        var user = userRepository.findByUsername(loginReq.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        boolean authenticated = passwordEncoder.matches(loginReq.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        var token = generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .success(true)
                .build();
    }

    @Override
    public IntrospectResponse Introspect(IntrospectReq introspectReq) throws JOSEException, ParseException {
        var token = introspectReq.getToken();
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified  = signedJWT.verify(verifier);
        return  IntrospectResponse.builder()
                .valid(verified && expiration.after(new Date()))
                .build();
    }

    String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("EASYODERBA")
                .issueTime(new Date())
                .claim("scope", buildScope(user))
                .expirationTime(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes(StandardCharsets.UTF_8)));

            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }

    }

    private  String  buildScope(User user) {
        StringJoiner scopeJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                scopeJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission -> {
                        scopeJoiner.add(permission.getName());
                    });
                }
            });
        }
        return scopeJoiner.toString();
    }
}
