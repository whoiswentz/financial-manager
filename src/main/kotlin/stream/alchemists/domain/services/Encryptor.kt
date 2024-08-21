package stream.alchemists.domain.services

interface Encryptor {
    fun encrypt(value: String): String
}