package com.hrapp.hrsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrapp.hrsystem.config.JwtAuthFilter;
import com.hrapp.hrsystem.dto.EmployeeRequestDTO;
import com.hrapp.hrsystem.dto.EmployeeResponseDTO;
import com.hrapp.hrsystem.event.EmployeeCreatedEvent;
import com.hrapp.hrsystem.mapper.EmployeeCreatedEventMapper;
import com.hrapp.hrsystem.producer.EmployeeEventProducer;
import com.hrapp.hrsystem.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EmployeeController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthFilter.class)
})
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private EmployeeEventProducer eventProducer;

    @MockBean
    private EmployeeCreatedEventMapper eventMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testCreateEmployee_withJobPositionId() throws Exception {
        EmployeeRequestDTO requestDTO = EmployeeRequestDTO.builder()
                .name("Julio")
                .email("julio@example.com")
                .jobTitle("Backend Engineer")
                .hireDate(LocalDate.of(2025, 9, 21))
                .phoneNumber("+1234567890")
                .departmentId(1L)
                .roleIds(Set.of(1L, 2L))
                .jobPositionId(5L)
                .build();

        EmployeeResponseDTO responseDTO = EmployeeResponseDTO.builder()
                .id(100L)
                .name("Julio")
                .email("julio@example.com")
                .jobTitle("Backend Engineer")
                .hireDate(LocalDate.of(2025, 9, 21))
                .phoneNumber("+1234567890")
                .departmentId(1L)
                .roleIds(Set.of(1L, 2L))
                .jobPositionTitle("Backend Engineer II")
                .jobPositionLevel(2)
                .build();

        Mockito.when(employeeService.createEmployee(Mockito.any())).thenReturn(responseDTO);
        Mockito.when(eventMapper.toEvent(Mockito.any())).thenReturn(new EmployeeCreatedEvent());

        Mockito.doNothing().when(eventProducer).sendEmployeeCreatedEvent(Mockito.any());

        mockMvc.perform(post("/api/employees")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(print())
                .andExpect(status().isCreated());

    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testUpdateEmployee_withJobPositionId() throws Exception {
        Long employeeId = 10L;

        EmployeeRequestDTO requestDTO = EmployeeRequestDTO.builder()
                .name("Julio Updated")
                .email("julio.updated@example.com")
                .jobTitle("Senior Engineer")
                .hireDate(LocalDate.of(2025, 9, 21))
                .phoneNumber("+1987654321")
                .departmentId(2L)
                .roleIds(Set.of(3L))
                .jobPositionId(7L)
                .build();

        EmployeeResponseDTO responseDTO = EmployeeResponseDTO.builder()
                .id(employeeId)
                .name("Julio Updated")
                .email("julio.updated@example.com")
                .jobTitle("Senior Engineer")
                .hireDate(LocalDate.of(2025, 9, 21))
                .phoneNumber("+1987654321")
                .departmentId(2L)
                .roleIds(Set.of(3L))
                .jobPositionTitle("Lead Engineer")
                .jobPositionLevel(3)
                .build();

        Mockito.when(employeeService.updateEmployee(Mockito.eq(employeeId), Mockito.any())).thenReturn(responseDTO);

        mockMvc.perform(put("/api/employees/{id}", employeeId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk());
    }
}
