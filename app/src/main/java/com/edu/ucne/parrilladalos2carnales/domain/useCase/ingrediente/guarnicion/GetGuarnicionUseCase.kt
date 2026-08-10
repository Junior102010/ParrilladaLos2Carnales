package com.edu.ucne.parrilladalos2carnales.domain.useCase.ingrediente.guarnicion

import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Guarnicion
import com.edu.ucne.parrilladalos2carnales.domain.repository.ingrediente.GuarnicionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGuarnicionUseCase @Inject constructor(
    private val Repository: GuarnicionRepository
) {
    operator fun invoke(): Flow<List<Guarnicion>> {
        return Repository.getGuarniciones()
    }
}
