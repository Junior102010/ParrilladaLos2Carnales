package com.edu.ucne.parrilladalos2carnales.domain.useCase.ingrediente.guarnicion

import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Guarnicion
import com.edu.ucne.parrilladalos2carnales.domain.repository.ingrediente.GuarnicionRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveGuarnicionUseCase @Inject constructor(
    private val repository: GuarnicionRepository
){
    suspend operator fun invoke() : Flow<List<Guarnicion>> = repository.observeGuarnicion()
}