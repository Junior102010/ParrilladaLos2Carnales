package com.edu.ucne.parrilladalos2carnales.domain.useCase.ingrediente.guarnicion

import com.edu.ucne.parrilladalos2carnales.domain.repository.ingrediente.GuarnicionRepository
import jakarta.inject.Inject

class DeleteGuarnicionUseCase @Inject constructor(
    private val repository: GuarnicionRepository
){
    suspend operator fun invoke(id : Int) = repository.deleteGuarnicion(id)
}