package science.credo.credomobiledetektor.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_detection.view.*
import science.credo.credomobiledetektor.R
import science.credo.credomobiledetektor.fragment.detections.DetectionContent

class DetectionAdapter(
        private val mValues: List<DetectionContent.HitItem>,
        private val mListener: DetectionFragment.OnListFragmentInteractionListener?
) : RecyclerView.Adapter<DetectionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_detection, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = mValues[position]
        holder.mIdView.text = holder.mItem!!.id
        holder.mContentView.text = holder.mItem!!.content
        val dataString = Base64.decode(holder.mItem!!.frame, Base64.DEFAULT)

        val img = BitmapFactory.decodeByteArray(dataString, 0, dataString.size)

        var scaleFactor = 2
        while (img.width * scaleFactor < 320) {
            scaleFactor *= 2
        }

        holder.mHit.setImageBitmap(Bitmap.createScaledBitmap(img, img.width * scaleFactor, img.height * scaleFactor, false))
        holder.mSizeView.text = holder.mSizeView.context.getString(R.string.detections_item_size, img.width, img.height)
        holder.mPositionView.text = holder.mSizeView.context.getString(R.string.detections_item_pos, holder.mItem!!.hit.mX, holder.mItem!!.hit.mY)
        holder.mMaxView.text = holder.mSizeView.context.getString(R.string.detections_item_max, holder.mItem!!.hit.mMaxValue)
        holder.mAverageView.text = holder.mSizeView.context.getString(R.string.detections_item_average, holder.mItem!!.hit.mAverage)
        holder.mBlacksView.text = holder.mSizeView.context.getString(R.string.detections_item_blacks, holder.mItem!!.hit.mBlacks, holder.mItem!!.hit.mBlackThreshold)

        holder.mView.setOnClickListener {
            mListener?.onListFragmentInteraction(holder.mItem)
        }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.pk
        val mContentView: TextView = mView.content
        val mSizeView: TextView = mView.size
        val mPositionView: TextView = mView.position
        val mMaxView: TextView = mView.maxBright
        val mAverageView: TextView = mView.average
        val mBlacksView: TextView = mView.blacks
        val mHit: ImageView = mView.hit
        var mItem: DetectionContent.HitItem? = null
    }
}
