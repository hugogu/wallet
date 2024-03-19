package io.hugo.wallet.stream

import io.hugo.wallet.model.event.AccountBalanceActivity
import io.hugo.wallet.model.event.FraudAlert
import org.apache.flink.api.common.functions.OpenContext
import org.apache.flink.api.common.state.ValueState
import org.apache.flink.api.common.state.ValueStateDescriptor
import org.apache.flink.api.common.typeinfo.Types
import org.apache.flink.api.java.tuple.Tuple2
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.util.Collector
import java.math.BigDecimal
import java.util.*

class FraudDetector : KeyedProcessFunction<UUID?, AccountBalanceActivity, FraudAlert?>() {

    @Transient
    private lateinit var continuousAmountState: ValueState<Tuple2<BigDecimal, Int>>

    @Throws(Exception::class)
    override fun processElement(
        transaction: AccountBalanceActivity,
        context: Context,
        collector: Collector<FraudAlert?>
    ) {
        val amountCount = continuousAmountState.value()
        if (amountCount == null) {
            val firstAmountCount = Tuple2(transaction.amount.number.numberValue(BigDecimal::class.java), 1)
            continuousAmountState.update(firstAmountCount)
            context.timerService().registerEventTimeTimer(context.timestamp() + ONE_MINUTE)
            return
        } else {
            val lastAmount = amountCount.f0 as BigDecimal
            val transactionAmount = transaction.amount.number.numberValue(BigDecimal::class.java)
            if (transactionAmount == lastAmount) {
                val newAmountCount = Tuple2(lastAmount, amountCount.f1 + 1)
                continuousAmountState.update(newAmountCount)
                if (newAmountCount.f1 >= 3) {
                    val alert = FraudAlert(transaction.accountId, transaction.transactionId)
                    collector.collect(alert)
                }
            } else {
                val newAmountCount = Tuple2(transactionAmount, 1)
                continuousAmountState.update(newAmountCount)
            }
        }
    }

    override fun open(openContext: OpenContext) {
        continuousAmountState = runtimeContext
            .getState(
                ValueStateDescriptor(
                    "continuousAmountState",
                    Types.TUPLE(Types.BIG_DEC, Types.INT)
                )
            )
    }

    override fun onTimer(timestamp: Long, ctx: OnTimerContext, out: Collector<FraudAlert?>) {
        continuousAmountState.clear()
    }

    companion object {
        private const val serialVersionUID = 1L

        private const val ONE_MINUTE = (60 * 1000).toLong()
    }
}