import org.w3c.dom.*
import org.w3c.dom.events.EventListener
import kotlin.browser.document
import kotlin.browser.window
import kotlin.random.Random

//Variáveis Globais
val posicoesGanharJogo = listOf(listOf(0,1,2), listOf(3,4,5), listOf(6,7,8), listOf(0,3,6), listOf(1,4,7),
        listOf(2,5,8), listOf(0,4,8), listOf(2,4,6))

val casas = document.getElementsByClassName("casa").asList()

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
        turnoJogador(idToNumber(target.id), "X")
        if(!temosUmGanhador())
            turnoOponente()
    }
}

//Função para executar a jogada randômica do computador
fun turnoOponente (){
    removerEventListeners()
    window.setTimeout({
        turnoJogador(EscolhaDoOponente(),"O")
        if(!temosUmGanhador())
            ativarEventListeners()
        }, 500)
}

//Função para finalizar a partida
fun fimDoJogo(list: List<Element>){
    list.forEach { casaGanhadora -> casaGanhadora.setAttribute("Style", "Color:Red")}
    removerEventListeners()
    window.setTimeout({
        if(list.get(0).innerHTML == "X")
            window.confirm("Você Ganhou o Jogo! Clique para jogar novamente!")
        else
            window.confirm("Você Perdeu o Jogo! Clique para jogar Novamente!")
        list.forEach { casa -> casa.removeAttribute("style") }
        limparTela()
        ativarEventListeners()
    }, 500)
}

//Função para verificar se temos uma sequência ganhadora
fun temosUmGanhador(): Boolean{

    var vitoria = false
    posicoesGanharJogo.forEach { comb -> run {
        val casasLocal = casas
        val sequencia = listOf(casasLocal.get(comb.get(0)), casasLocal.get(comb.get(1)), casasLocal.get(comb.get(2)))
        if(iguais(sequencia)) {
            vitoria = true
            fimDoJogo(sequencia)
        }
    }}
    if(casasVazias().size == 0 && vitoria == false){
        window.confirm("Empate! Clique para jogar novamente!")
        limparTela()
        ativarEventListeners()
    }
    return vitoria
}

//Funções Auxiliares

fun idToNumber (id: String) = id.replace("casa", "").toInt()

fun casasVazias() = casas.filter{casa -> casa.innerHTML == ""}

fun iguais(list: List<Element>) = list.all { casa -> casa.innerHTML == list.get(0).innerHTML && casa.innerHTML != ""}

fun turnoJogador (index: Int, letra: String) {
    casas.get(index).innerHTML = letra
}

fun EscolhaDoOponente() =
        if(casasVazias().size == 1)
            idToNumber(casasVazias().get(0).id)
        else
            idToNumber(casasVazias().get(Random.nextInt(casasVazias().size - 1)).id)

fun limparTela () = casas.forEach { casa -> casa.innerHTML = ""}
