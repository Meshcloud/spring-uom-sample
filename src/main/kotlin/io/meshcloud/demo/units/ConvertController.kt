package io.meshcloud.demo.units

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import javax.measure.Quantity

@Controller
class ConvertController {
  @PostMapping("/convert")
  fun convert(@RequestBody model: ConvertRequestModel): ResponseEntity<ConvertResponseModel> {
    val result = model.quantity.convertTo(model.target)
    return ResponseEntity.ok(ConvertResponseModel(result))
  }
}

data class ConvertRequestModel(
    val quantity: Quantity<*>,
    val target: javax.measure.Unit<*>
)

data class ConvertResponseModel(
    val result: Quantity<*>
)