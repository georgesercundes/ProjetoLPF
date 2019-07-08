import org.w3c.dom.*
import org.w3c.dom.events.EventListener
import kotlin.browser.document
import kotlin.browser.window
import kotlin.random.Random

//Variáveis Globais
val posicoesGanharJogo = listOf(listOf(0,1,2), listOf(3,4,5), listOf(6,7,8), listOf(0,3,6), listOf(1,4,7),
        listOf(2,5,8), listOf(0,4,8), listOf(2,4,6))

val casas = document.getElementsByClassName("casa").asList()
val jogador = "X"
val computador = "O"

//Função Principal
fun main(){
    ativarEventListeners()
}

//Funções para ativar e remover a opção de receber um clique nas casas do jogo
fun ativarEventListeners() = casas.forEach { casa -> casa.addEventListener("click", listener)}

fun removerEventListeners() = casas.forEach { casa -> casa.removeEventListener("click", listener)}

//Função para executar a jogada após o clique
val listener = EventListener {
    val target = it.target as HTMLElement
    if(target.innerHTML == ""){
        turnoJogador(idToNumber(target.id), jogador)
        if(!temosUmGanhador() && !empate())
            turnoOponente() // Passa a jogada pro computador
    }
}

//Função para executar a jogada do computador
fun turnoOponente (){
    removerEventListeners()
    window.setTimeout({
        turnoJogador(EscolhaDoOponente(), computador)
        if(!temosUmGanhador() && !empate())
            ativarEventListeners()
        }, 500)
}

//Função para escolher a jogada do computador
fun EscolhaDoOponente(): Int {
    posicoesGanharJogo.forEach { comb -> run {
        val casasLocal = casas
        val sequencia = listOf(casasLocal.get(comb.get(0)), casasLocal.get(comb.get(1)), casasLocal.get(comb.get(2)))
        val casasVaziasSequencia = sequencia.filter { casa -> casa.innerHTML == "" }
        val casasSobrando = sequencia.filter { casa -> casa.innerHTML != "" }
        if(casasVaziasSequencia.size == 1 && casasSobrando.all { casa -> casa.innerHTML == computador })
            return idToNumber(casasVaziasSequencia.get(0).id)
        if(casasVaziasSequencia.size == 1 && casasSobrando.all { casa -> casa.innerHTML == jogador })
            return idToNumber(casasVaziasSequencia.get(0).id)
    } }

    if(casasVazias().size == 1)
        return idToNumber(casasVazias().get(0).id)
    else
        return idToNumber(casasVazias().get(Random.nextInt(casasVazias().size - 1)).id)

}

//Função para finalizar a partida
fun fimDoJogo(list: List<Element>){
    list.forEach { casaGanhadora -> casaGanhadora.setAttribute("Style", "Color:Red")}
    removerEventListeners()
    var confirma: Boolean
    window.setTimeout({
        if(list.get(0).innerHTML == jogador){
            confirma = window.confirm("Você Ganhou o Jogo! Clique para jogar novamente!")
            if(confirma){
                list.forEach { casa -> casa.removeAttribute("style") }
                limparTela()
                ativarEventListeners()
            }
        }

        else{
            confirma = window.confirm("Você Perdeu o Jogo! Clique para jogar Novamente!")
            if(confirma){
                list.forEach { casa -> casa.removeAttribute("style") }
                limparTela()
                ativarEventListeners()
            }
        }
    }, 500)
}

//Função para verificar se temos um ganhador
fun temosUmGanhador(): Boolean{
    posicoesGanharJogo.forEach { comb -> run {
        val casasLocal = casas
        val sequencia = listOf(casasLocal.get(comb.get(0)), casasLocal.get(comb.get(1)), casasLocal.get(comb.get(2)))
        if(iguais(sequencia)) {
            fimDoJogo(sequencia)
            return true
        }
    }}
    return false
}

fun empate (): Boolean {
    if(casasVazias().size == 0) {
        val confirma = window.confirm("Empate! Clique para jogar novamente!")
        if(confirma)
            limparTela()
        return true
    }
    return false
}

//Funções Auxiliares

fun idToNumber (id: String) = id.replace("casa", "").toInt()

fun casasVazias() = casas.filter{casa -> casa.innerHTML == ""}

fun iguais(list: List<Element>) = list.all { casa -> casa.innerHTML == list.get(0).innerHTML && casa.innerHTML != ""}

fun turnoJogador (index: Int, letra: String) {
    casas.get(index).innerHTML = letra
}

fun limparTela () = casas.forEach { casa -> casa.innerHTML = ""}