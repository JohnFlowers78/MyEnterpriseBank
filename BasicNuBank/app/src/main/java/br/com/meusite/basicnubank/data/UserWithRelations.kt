package br.com.meusite.basicnubank.data

import androidx.room.Embedded
import androidx.room.Relation
import br.com.meusite.basicnubank.data.caixinha.Caixinha
import br.com.meusite.basicnubank.data.transacao.Transacao
import br.com.meusite.basicnubank.data.user.User

data class UserWithRelations(
    @Embedded val user: User,     // aqui "user" contem um objeto "User"  --> p/ usar algum atributo de User --> user.user.id
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val transacoes: List<Transacao>,

    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val caixinhas: List<Caixinha>
)