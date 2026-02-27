//package com.epam.meetingroombooking.controller;
//
//import com.epam.meetingroombooking.dto.AuthDto;
//import com.epam.meetingroombooking.model.User;
//import com.epam.meetingroombooking.security.JwtTokenProvider;
//import com.epam.meetingroombooking.service.UserService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.Collections;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//class AuthControllerTest {
//
//    private MockMvc mockMvc;
//
//    @Mock
//    private AuthenticationManager authenticationManager;
//
//    @Mock
//    private UserService userService;
//
//    @Mock
//    private JwtTokenProvider tokenProvider;
//
//    @InjectMocks
//    private AuthController authController;
//
//    private ObjectMapper objectMapper = new ObjectMapper();
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
//    }
//
//    @Test
//    void login_ShouldReturnJwtResponse() throws Exception {
//        AuthDto.LoginRequest loginRequest = new AuthDto.LoginRequest();
//        loginRequest.setUsername("testuser");
//        loginRequest.setPassword("password");
//
//        Authentication authentication = mock(Authentication.class);
//        when(authentication.getAuthorities()).thenReturn((any()));
//        // Use doReturn or more specific mocking for authorities
//        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
//        when(authentication.getAuthorities()).thenAnswer(inv -> Collections.singletonList(authority));
//
//        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
//                .thenReturn(authentication);
//        when(tokenProvider.generateToken(any(Authentication.class))).thenReturn("test-jwt-token");
//
//        mockMvc.perform(post("/api/auth/login")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(loginRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.token").value("test-jwt-token"))
//                .andExpect(jsonPath("$.username").value("testuser"))
//                .andExpect(jsonPath("$.role").value("ROLE_USER"));
//    }
//
//    @Test
//    void register_ShouldReturnSuccessMessage() throws Exception {
//        AuthDto.RegisterRequest registerRequest = new AuthDto.RegisterRequest();
//        registerRequest.setUsername("newuser");
//        registerRequest.setPassword("password");
//        registerRequest.setEmail("newuser@example.com");
//        registerRequest.setAdmin(false);
//
//        mockMvc.perform(post("/api/auth/register")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(registerRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").value("User registered successfully"));
//    }
//}
