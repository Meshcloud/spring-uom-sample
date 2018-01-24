package io.meshcloud.demo.units

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.json.PackageVersion
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import org.springframework.stereotype.Service
import javax.measure.Quantity
import javax.measure.Unit

@Service // this will make this module available in the ObjectMapper used in Springs Http pipeline
class JacksonUomModule : SimpleModule(PackageVersion.VERSION) {
  companion object {
    private val serialVersionUID = 1L
  }

  override fun setupModule(context: SetupContext) {
    addSerializer(Quantity::class.java, QuantitySerializer)
    addDeserializer(Quantity::class.java, QuantityDeserializer)
    addSerializer(Unit::class.java, UnitSerializer)
    addDeserializer(Unit::class.java, UnitDeserializer)
    super.setupModule(context)
  }

  object QuantitySerializer : JsonSerializer<Quantity<*>>() {
    override fun handledType(): Class<Quantity<*>> {
      return Quantity::class.java
    }

    override fun serialize(value: Quantity<*>, gen: JsonGenerator, serializers: SerializerProvider?) {
      val formatted = UomFormatting.QuantityToStringConverter.convert(value)
      gen.writeString(formatted)
    }
  }

  object QuantityDeserializer : JsonDeserializer<Quantity<*>>() {
    override fun handledType(): Class<Quantity<*>> {
      return Quantity::class.java
    }

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): Quantity<*>? {
      val source = p.valueAsString
      return UomFormatting.StringToQuantityConverter.convert(source)
    }
  }

  object UnitSerializer : JsonSerializer<Unit<*>>() {
    override fun handledType(): Class<Unit<*>> {
      return Unit::class.java
    }

    override fun serialize(value: Unit<*>, gen: JsonGenerator, serializers: SerializerProvider?) {
      val formatted = UomFormatting.UnitToStringConverter.convert(value)
      gen.writeString(formatted)
    }
  }

  object UnitDeserializer : JsonDeserializer<Unit<*>>() {
    override fun handledType(): Class<Unit<*>> {
      return Unit::class.java
    }

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): Unit<*>? {
      val source = p.valueAsString
      return UomFormatting.StringToUnitConverter.convert(source)
    }
  }
}


