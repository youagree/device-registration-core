package ru.unit.techno.device.registration.core.impl.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.unit.techno.device.registration.core.impl.dto.RegistrationDto;
import ru.unit.techno.device.registration.core.impl.service.RegistrationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public Long createUser(@RequestBody RegistrationDto registrationDto) {
        return registrationService.registerGroup(registrationDto);
    }

    @GetMapping("/test")
    public String testController() {
        return "success";
    }
}
