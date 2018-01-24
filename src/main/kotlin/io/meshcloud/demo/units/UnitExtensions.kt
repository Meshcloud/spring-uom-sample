package io.meshcloud.demo.units

import org.tenkiv.physikal.core.convertTo
import org.tenkiv.physikal.core.invoke
import systems.uom.quantity.Information
import systems.uom.ucum.UCUM
import tec.uom.se.AbstractUnit
import tec.uom.se.ComparableQuantity
import tec.uom.se.quantity.Quantities
import javax.measure.Quantity
import javax.measure.Unit
import javax.measure.quantity.Dimensionless

/**
 * Builder method for [ComparableQuantity] with unit [ONE].
 *
 * @return A [ComparableQuantity] with specified value.
 */
val Number.instance: ComparableQuantity<Dimensionless>
  get() = Quantities.getQuantity<Dimensionless>(this, AbstractUnit.ONE)

fun Quantity<*>.toWildcard(): Quantity<Nothing> {
  @Suppress("UNCHECKED_CAST")
  return this as Quantity<Nothing>
}

fun Unit<*>.toWildcard(): Unit<Nothing> {
  @Suppress("UNCHECKED_CAST")
  return this as Unit<Nothing>
}

val Number.yobi get() = BinaryPrefixedNumber(this, BinaryPrefix.YOBI)
val Number.zebi get() = BinaryPrefixedNumber(this, BinaryPrefix.ZEBI)
val Number.exbi get() = BinaryPrefixedNumber(this, BinaryPrefix.EXBI)
val Number.pebi get() = BinaryPrefixedNumber(this, BinaryPrefix.PEBI)
val Number.tebi get() = BinaryPrefixedNumber(this, BinaryPrefix.TEBI)
val Number.gibi get() = BinaryPrefixedNumber(this, BinaryPrefix.GIBI)
val Number.mebi get() = BinaryPrefixedNumber(this, BinaryPrefix.MEBI)
val Number.kibi get() = BinaryPrefixedNumber(this, BinaryPrefix.KIBI)

val BinaryPrefixedNumber.byte: ComparableQuantity<Information>
  get() = number(UCUM.BYTE.transform(prefix.converter))

fun Quantity<*>.convertTo(target: javax.measure.Unit<*>): javax.measure.Quantity<Nothing> {
  val targetUnit = target.toWildcard()
  return this.toWildcard().convertTo(targetUnit)
}

data class BinaryPrefixedNumber(val number: Number, val prefix: BinaryPrefix)