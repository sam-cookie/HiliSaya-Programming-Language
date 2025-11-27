package errorhandling

open class HiliSayaError(message: String, val line: Int) : Exception(message)

class ScanError(message: String, line: Int) : HiliSayaError(message, line)

class ParseError(message: String, line: Int) : HiliSayaError(message, line)

class RuntimeError(message: String, line: Int) : HiliSayaError(message, line)
