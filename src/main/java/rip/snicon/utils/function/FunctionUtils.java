package rip.snicon.utils.function;

import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import rip.snicon.Main;

import java.lang.reflect.Method;
import java.util.Arrays;

public class FunctionUtils {

    private Player player;
    private Event event;

    public FunctionUtils(Player player, Event event, String function) {
        // function is a string with first the package name of a class
        // then the class name followed by a static method with args. Example:
        // rip.snicon.utils.EmeraldUtils.addEmeralds(player, 2)
        // The args then follow the same structure as the real class.

        this.player = player;
        this.event = event;
        invokeFunction(function);
    }

    public void invokeFunction(String function) {
        try {
            // Validate that the function string is not null and contains a '.'
            if (function == null || !function.contains(".")) {
                throw new IllegalArgumentException("Invalid function string: " + function);
            }

            // Split the method call string to extract class name and method details
            String[] parts = function.split("\\.");
            if (parts.length < 2) {
                throw new IllegalArgumentException("Invalid function format. Expected 'ClassName.MethodName(args)'.");
            }

            // Construct the fully qualified class name
            String className = String.join(".", Arrays.copyOf(parts, parts.length - 1));
            String methodWithArgs = parts[parts.length - 1];

            // Ensure parentheses are present
            if (!methodWithArgs.contains("(")) {
                methodWithArgs += "()";  // Assume no arguments if parentheses are missing
            }

            // Extract method name and argument string safely
            String[] methodParts = methodWithArgs.split("\\(");
            if (methodParts.length < 2) {
                throw new IllegalArgumentException("Invalid method call format. Expected 'MethodName(args)'.");
            }

            String methodName = methodParts[0];
            String argsString = methodParts[1].replace(")", "");

            // Split arguments, handle empty arguments case
            String[] argNames = argsString.isEmpty() ? new String[0] : argsString.split(",");

            // Prepare arguments
            Object[] arguments = new Object[argNames.length];
            Class<?>[] paramTypes = new Class<?>[argNames.length];

            for (int i = 0; i < argNames.length; i++) {
                argNames[i] = argNames[i].trim();

                if (argNames[i].equalsIgnoreCase("player")) {
                    arguments[i] = this.player;
                    paramTypes[i] = Player.class;
                } else if (argNames[i].equalsIgnoreCase("event")) {
                    arguments[i] = this.event;
                    paramTypes[i] = Event.class;
                } else {
                    arguments[i] = argNames[i]; // Assume remaining arguments are strings
                    paramTypes[i] = String.class;
                }
            }

            // Dynamically load the class using the fully qualified name
            Class<?> cls = Class.forName(className);

            // Handle static method invocation
            Method method = cls.getMethod(methodName, paramTypes);
            if (!java.lang.reflect.Modifier.isStatic(method.getModifiers())) {
                throw new IllegalArgumentException("Method " + methodName + " is not static.");
            }
            method.invoke(null, arguments); // Static method, no instance required

        } catch (Exception e) {
            e.printStackTrace();
            Main.logger.error("Error finding Class from function string {}, {}", function, e.getMessage());
        }
    }

}
