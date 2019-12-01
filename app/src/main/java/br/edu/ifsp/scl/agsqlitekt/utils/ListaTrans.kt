package br.edu.ifsp.scl.agsqlitekt.utils

import br.edu.ifsp.scl.agsqlitekt.model.Transacao

class ListaTrans() {

        fun getPositionlist(lista: MutableList<Transacao>, id: Int): Int{
            var pos: Int = -1
            var t: Int
            var size = lista.size

            for(index in 0..lista.size-1){
                t = lista[index].id
                if(t.equals(id)){
                    pos = index
                }
            }

            return pos
        }

}