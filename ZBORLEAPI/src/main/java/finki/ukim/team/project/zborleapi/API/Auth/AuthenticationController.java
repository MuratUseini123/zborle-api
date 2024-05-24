package finki.ukim.team.project.zborleapi.API.Auth;

import finki.ukim.team.project.zborleapi.Model.DTO.Request.AuthenticationRequest;
import finki.ukim.team.project.zborleapi.Model.DTO.Request.ChangePasswordRequest;
import finki.ukim.team.project.zborleapi.Model.DTO.Request.RegisterRequest;
import finki.ukim.team.project.zborleapi.Model.DTO.Response.AuthenticationResponse;
import finki.ukim.team.project.zborleapi.Service.AuthenticationService;
import finki.ukim.team.project.zborleapi.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication API", description = "API for managing user authentication")
public class AuthenticationController {
    private final AuthenticationService service;
    private final UserService userService;

    @Operation(summary = "Register a new user")
    @ApiResponse(responseCode = "200", description = "User registered successfully")
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    @Operation(summary = "Authenticate user")
    @ApiResponse(responseCode = "200", description = "User authenticated successfully")
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @Operation(summary = "Refresh authentication token")
    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }

    @Operation(summary = "Change user password")
    @ApiResponse(responseCode = "200", description = "Password changed successfully")
    @PatchMapping("/user/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        userService.changePassword(request, connectedUser);

        return ResponseEntity.ok().build();
    }
}