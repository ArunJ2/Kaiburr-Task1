package com.kaiburr.task_manager.service;

import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

@Service
public class CommandValidationService {
    
    private static final List<String> UNSAFE_COMMANDS = Arrays.asList(
        "rm", "delete", "format", "shutdown", "reboot", "mkfs", "dd",
        ">", "|", "&", "&&", "||", ";", "`", "$", "(", ")", "{", "}"
    );
    
    public boolean isCommandSafe(String command) {
        if (command == null || command.trim().isEmpty()) {
            return false;
        }
        
        String lowerCommand = command.toLowerCase();
        return UNSAFE_COMMANDS.stream()
                .noneMatch(unsafe -> lowerCommand.contains(unsafe.toLowerCase()));
    }
}