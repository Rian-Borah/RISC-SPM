package Register_Unit

import chisel3._
import chisel3.util._

class ProgramCounter(wordSize: Int) extends Module{
    val io = IO(new Bundle{
        val count = Output(wordSize.W)
        val dataIn = Input(wordSize.W)
        val loadPC = Input(Bool())
        val incPC = Input(Bool())
    })

    val PC = RegInit(0.U(wordSize.W))

    when(io.loadPC){
        PC := io.dataIn
    }.otherwise(io.incPC){
        PC := PC + 1.U
    }
}