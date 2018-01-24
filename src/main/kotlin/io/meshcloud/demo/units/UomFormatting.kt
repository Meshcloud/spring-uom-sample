package io.meshcloud.demo.units

import org.springframework.core.convert.converter.Converter
import systems.uom.ucum.format.UCUMFormat
import tec.uom.se.quantity.Quantities
import java.lang.Appendable
import java.text.NumberFormat
import java.util.*
import javax.measure.Quantity
import javax.measure.Unit
import javax.measure.format.UnitFormat

object UomFormatting {
  private val unitFormatter = UcumFormatWithBinaryPrefixSupport()

  private val numberFormatter = NumberFormat.getInstance(Locale.ROOT)
  private val separator = " "

  object QuantityToStringConverter : Converter<Quantity<*>, String> {
    override fun convert(source: Quantity<*>): String {
      val s = source.toWildcard()
      val formattedUnit = unitFormatter.format(s.unit)
      val formattedValue = numberFormatter.format(s.value)

      return "$formattedValue${separator}$formattedUnit"
    }
  }

  object StringToQuantityConverter : Converter<String, Quantity<*>> {
    override fun convert(source: String): Quantity<*> {
      val (formattedValue, formattedUnit) = source.split(separator, limit = 2)

      val parsedUnit: Unit<Nothing> = unitFormatter.parse(formattedUnit).toWildcard()
      val parsedValue = numberFormatter.parse(formattedValue)

      return Quantities.getQuantity(parsedValue, parsedUnit)
    }
  }

  object UnitToStringConverter : Converter<Unit<*>, String> {
    override fun convert(source: Unit<*>): String {
      return unitFormatter.format(source)
    }
  }

  object StringToUnitConverter : Converter<String, Unit<*>> {
    override fun convert(source: String): Unit<*> {
      return unitFormatter.parse(source).toWildcard()
    }
  }

  class UcumFormatWithBinaryPrefixSupport(val base: UCUMFormat = UCUMFormat.getInstance(UCUMFormat.Variant.CASE_SENSITIVE))
    : UnitFormat by base {

    override fun format(unit: Unit<*>): String {
      val baseResult = base.format(unit)
      val split = baseResult.split(".")

      if (split.size == 1) {
        return baseResult
      }

      val (symbol, converter) = split
      return when (converter) {
        BinaryPrefix.YOBI.converterFormat -> "${BinaryPrefix.YOBI.symbol}$symbol"
        BinaryPrefix.ZEBI.converterFormat -> "${BinaryPrefix.ZEBI.symbol}$symbol"
        BinaryPrefix.EXBI.converterFormat -> "${BinaryPrefix.EXBI.symbol}$symbol"
        BinaryPrefix.PEBI.converterFormat -> "${BinaryPrefix.PEBI.symbol}$symbol"
        BinaryPrefix.TEBI.converterFormat -> "${BinaryPrefix.TEBI.symbol}$symbol"
        BinaryPrefix.GIBI.converterFormat -> "${BinaryPrefix.GIBI.symbol}$symbol"
        BinaryPrefix.MEBI.converterFormat -> "${BinaryPrefix.MEBI.symbol}$symbol"
        BinaryPrefix.KIBI.converterFormat -> "${BinaryPrefix.KIBI.symbol}$symbol"
        else -> baseResult
      }
    }

    override fun format(unit: Unit<*>, appendable: Appendable): Appendable {
      return appendable.append(format(unit))
    }

    override fun parse(csq: CharSequence): Unit<*> {
      // note: all binary prefixes have two chars
      val prefixLength = 2
      val (prefix, originalUnit) = when {
        csq.length >= prefixLength -> Pair(csq.substring(0, prefixLength), csq.substring(prefixLength))
        else -> Pair(null, csq)
      }

      return when (prefix) {
        BinaryPrefix.YOBI.symbol -> base.parse(originalUnit).transform(BinaryPrefix.YOBI.converter)
        BinaryPrefix.ZEBI.symbol -> base.parse(originalUnit).transform(BinaryPrefix.ZEBI.converter)
        BinaryPrefix.EXBI.symbol -> base.parse(originalUnit).transform(BinaryPrefix.EXBI.converter)
        BinaryPrefix.PEBI.symbol -> base.parse(originalUnit).transform(BinaryPrefix.PEBI.converter)
        BinaryPrefix.TEBI.symbol -> base.parse(originalUnit).transform(BinaryPrefix.TEBI.converter)
        BinaryPrefix.GIBI.symbol -> base.parse(originalUnit).transform(BinaryPrefix.GIBI.converter)
        BinaryPrefix.MEBI.symbol -> base.parse(originalUnit).transform(BinaryPrefix.MEBI.converter)
        BinaryPrefix.KIBI.symbol -> base.parse(originalUnit).transform(BinaryPrefix.KIBI.converter)
        else -> base.parse(csq)
      }
    }
  }
}