package stream.alchemists.infrastructure.services

import io.ktor.util.*
import stream.alchemists.AppConfiguration
import stream.alchemists.domain.services.Encryptor
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

class EncryptorImpl : Encryptor {
    private val secretKey = AppConfiguration.secretKey
    private val algorithm = AppConfiguration.algorithm
    private val iterations = AppConfiguration.iterations
    private val keyLength = AppConfiguration.keyLength

    override fun encrypt(value: String): String {
        val salt = ByteArray(32)
        SecureRandom().nextBytes(salt)
        val hash = hash(value, salt)
        return "${Base64.getEncoder().encodeToString(salt)}:${hex(hash)}"
    }

    override fun verify(value: String, encrypted: String): Boolean {
        return try {
            val parts = encrypted.split(":")
            if (parts.size != 2) return false
            val salt = Base64.getDecoder().decode(parts[0])
            val expectedHash = parts[1]
            val actualHash = hex(hash(value, salt))
            constantTimeEquals(expectedHash, actualHash)
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    private fun constantTimeEquals(a: String, b: String): Boolean {
        if (a.length != b.length) return false
        var result = 0
        for (i in a.indices) {
            result = result or (a[i].code xor b[i].code)
        }
        return result == 0
    }

    private fun hash(value: String, salt: ByteArray): ByteArray {
        val combinedSalt = salt + secretKey.toByteArray()
        val factory = SecretKeyFactory.getInstance(algorithm)
        val spec = PBEKeySpec(value.toCharArray(), combinedSalt, iterations, keyLength)
        return factory.generateSecret(spec).encoded
    }
}
