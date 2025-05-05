package utils

import org.mindrot.jbcrypt.BCrypt
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.util.*

/**
 * The `PasswordGenerator` class provides methods to generate strong, random passwords based on various criteria.
 * It supports including lowercase letters, uppercase letters, digits, and special symbols in the generated passwords.
 * The class uses the [SecureRandom] class for cryptographically secure random number generation.
 */
object PasswordUtils {
    // Character classes to be used
    private const val UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private const val LETTERS = "abcdefghijklmnopqrstuvwxyz"
    private const val DIGITS = "0123456789"
    private const val SPECIAL = "!@#$%^&*()-_+=<>?"

    /**
     * Generates a random password based on the specified criteria.
     * @param hasLetters Boolean value to specify if the password must contain lowercase letters
     * @param hasUppercaseLetters Boolean value to specify if the password must contain uppercase letters
     * @param hasDigits Specifies whether the password should contain digits (0-9).
     * @param hasSpecialSymbols Specifies whether the password should contain special symbols (e.g., @, #, $).
     * @param length The desired length of the password.
     * @throws IllegalArgumentException if the length is less than 6.
     * @return A randomly generated password that meets the specified criteria.
     */
    @JvmOverloads
    fun generatePassword(
        hasLetters: Boolean = true,
        hasUppercaseLetters: Boolean = true,
        hasDigits: Boolean = true,
        hasSpecialSymbols: Boolean = true,
        length: Int = 24
    ): String {
        require(length >= 6) { "Password length can not be less than 6 characters" }

        // List to store characters to be used to contract the password
        val characters: MutableList<Char?> = ArrayList<Char?>()

        // if the password must contain lowercase letters, add them to the `characters` list
        if (hasLetters) {
            for (i in LETTERS.length - 1 downTo 0) {
                characters.add(LETTERS[i])
            }
        }
        // if the password must contain uppercase letters, add them to the `characters` list
        if (hasUppercaseLetters) {
            for (i in UPPERCASE_LETTERS.length - 1 downTo 0) {
                characters.add(UPPERCASE_LETTERS[i])
            }
        }
        // if the password must contain digits, add them to the `characters` list
        if (hasDigits) {
            for (i in DIGITS.length - 1 downTo 0) {
                characters.add(DIGITS[i])
            }
        }
        // if the password must contain special chars, add them to the `characters` list
        if (hasSpecialSymbols) {
            for (i in SPECIAL.length - 1 downTo 0) {
                characters.add(SPECIAL[i])
            }
        }
        // Shuffling the characters before constructing the password
        characters.shuffle()
        val size = characters.size

        // Random number generator
        val secureRandom: SecureRandom?
        try {
            secureRandom = SecureRandom.getInstance("SHA1PRNG")
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        }

        // String builder to build the password string
        val stringBuilder = StringBuilder()

        // Get a new random number and get the character corresponding with the index in a character list,
        // then, add the character to the string builder.
        repeat(length) {
            val index = secureRandom.nextInt(size)
            val character: Char = characters[index]!!
            stringBuilder.append(character)
        }

        // Get the password string
        return stringBuilder.toString()
    }

    /**
     * Generates a random password with specified whether digits or special symbols should be included and the desired length of the password.
     *
     *
     *
     * This method uses the default character sets
     *
     *  * Includes lowercase letters (a-z)
     *  * Includes uppercase letters (A-Z)
     *
     *
     * @param hasDigits Specifies whether the password should contain digits (0-9).
     * @param hasSpecialSymbols Specifies whether the password should contain special symbols (e.g. !, @, #, $).
     * @param length The desired length of the password.
     * @throws IllegalArgumentException if the length is less than 6.
     * @return A randomly generated password that meets the specified criteria.
     */
    fun generatePassword(hasDigits: Boolean, hasSpecialSymbols: Boolean, length: Int) = generatePassword(
        hasLetters = true,
        hasUppercaseLetters = true,
        hasDigits = hasDigits,
        hasSpecialSymbols = hasSpecialSymbols,
        length = length
    )

    /**
     * Generates a random password with specified whether special symbols should be included and the desired length of the password.
     *
     *
     *
     * This method uses the default character sets
     *
     *  * Includes lowercase letters (a-z)
     *  * Includes uppercase letters (A-Z)
     *  * Includes digits (0-9)
     *
     *
     * @param hasSpecialSymbols Specifies whether the password should contain special symbols (e.g. !, @, #, $).
     * @param length The desired length of the password.
     * @throws IllegalArgumentException if the length is less than 6.
     * @return A randomly generated password that meets the specified criteria.
     */
    fun generatePassword(hasSpecialSymbols: Boolean, length: Int) = generatePassword(
        hasLetters = true,
        hasUppercaseLetters = true,
        hasDigits = true,
        hasSpecialSymbols = hasSpecialSymbols,
        length = length
    )

    /**
     * Generates a random password of the specified length.
     *
     *
     * This method uses the default character sets
     *
     *  * Includes lowercase letters (a-z)
     *  * Includes uppercase letters (A-Z)
     *  * Includes digits (0-9)
     *  * Includes special characters (!@#$%^&*()_+\-=[]{};':"|,.<>/?~)
     *
     *
     * @param length The desired length of the password. Must be a positive integer.
     * @throws IllegalArgumentException if the length is less than 6.
     * @return A randomly generated password that meets the specified criteria.
     */
    fun generatePassword(length: Int) = generatePassword(
        hasLetters = true,
        hasUppercaseLetters = true,
        hasDigits = true,
        hasSpecialSymbols = true,
        length = length
    )

    /**
     * Validates a plain text password against a hashed password.
     *
     * @param password The plain text password to be validated.
     * @param hashedPassword The hashed password to validate against.
     * @return True if the plain text password matches the hashed password, otherwise false.
     */
    fun validatePassword(password: String, hashedPassword: String): Boolean = BCrypt.checkpw(password, hashedPassword)

    /**
     * Encrypts the given plain text password and returns its hashed equivalent.
     * This method uses the BCrypt hashing algorithm to ensure the security
     * of the resulting hashed password.
     *
     * @param password The plain text password to be encrypted. Must not be null.
     * @return The hashed version of the input password.
     */
    fun encryptPassword(password: String): String = BCrypt.hashpw(password, BCrypt.gensalt())
}
