package io.meshcloud.demo.units

import tec.uom.lib.common.function.SymbolSupplier
import tec.uom.lib.common.function.UnitConverterSupplier
import tec.uom.se.function.RationalConverter
import java.math.BigInteger
import javax.measure.UnitConverter

enum class BinaryPrefix(
    private val symbol: String,
    private val converter: RationalConverter
) : SymbolSupplier, UnitConverterSupplier {

  YOBI("Yi", RationalConverter(BigInteger.valueOf(1024L).pow(8), BigInteger.ONE)),
  ZEBI("Zi", RationalConverter(BigInteger.valueOf(1024L).pow(7), BigInteger.ONE)),
  EXBI("Ei", RationalConverter(BigInteger.valueOf(1024L).pow(6), BigInteger.ONE)),
  PEBI("Pi", RationalConverter(BigInteger.valueOf(1024L).pow(5), BigInteger.ONE)),
  TEBI("Ti", RationalConverter(BigInteger.valueOf(1024L).pow(4), BigInteger.ONE)),
  GIBI("Gi", RationalConverter(BigInteger.valueOf(1024L).pow(3), BigInteger.ONE)),
  MEBI("Mi", RationalConverter(BigInteger.valueOf(1024L).pow(2), BigInteger.ONE)),
  KIBI("Ki", RationalConverter(BigInteger.valueOf(1024L).pow(1), BigInteger.ONE));

  override fun getSymbol(): String {
    return symbol
  }

  override fun getConverter(): UnitConverter {
    return converter
  }

  val converterFormat: String
    get() {
      return converter.dividend.toString()
    }
}