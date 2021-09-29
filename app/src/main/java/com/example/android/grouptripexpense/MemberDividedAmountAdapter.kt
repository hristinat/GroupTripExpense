package com.example.android.grouptripexpense

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.grouptripexpense.MemberDividedAmountAdapter.MemberDivideAmountViewHolder

class MemberDividedAmountAdapter : RecyclerView.Adapter<MemberDivideAmountViewHolder>() {
    private var memberDivideAmounts: List<MemberDividedAmount>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberDivideAmountViewHolder {
        val context = parent.context
        val layoutIdForListItem = R.layout.member_divided_amount
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(layoutIdForListItem, parent, false)
        return MemberDivideAmountViewHolder(view)
    }

    override fun onBindViewHolder(holder: MemberDivideAmountViewHolder, position: Int) {
        val memberDividedAmount = memberDivideAmounts!![position]
        holder.mMemberName.text = memberDividedAmount.memberName
        holder.mPaid.text = "%.2f".format(memberDividedAmount.paid)
        holder.mToPay.text = "%.2f".format(memberDividedAmount.toPay)
        holder.mToReceive.text = "%.2f".format(memberDividedAmount.toReceive)
    }

    override fun getItemCount(): Int {
        return memberDivideAmounts?.size ?: 0
    }

    fun setMemberDivideAmounts(memberDivideAmounts: List<MemberDividedAmount>?) {
        this.memberDivideAmounts = memberDivideAmounts
        notifyDataSetChanged()
    }

    inner class MemberDivideAmountViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mMemberName: TextView = itemView.findViewById(R.id.member_name)
        var mPaid: TextView = itemView.findViewById(R.id.paid)
        var mToPay: TextView = itemView.findViewById(R.id.to_pay)
        var mToReceive: TextView = itemView.findViewById(R.id.to_receive)
    }
}