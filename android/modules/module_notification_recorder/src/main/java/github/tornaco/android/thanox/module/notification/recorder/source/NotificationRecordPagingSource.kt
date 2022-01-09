package github.tornaco.android.thanox.module.notification.recorder.source

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult.Page
import androidx.paging.PagingState
import com.elvishew.xlog.XLog
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.n.NotificationRecord
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.core.util.DateUtils
import github.tornaco.android.thanox.module.notification.recorder.NotificationRecordModel
import github.tornaco.android.thanox.module.notification.recorder.R

class NotificationRecordPagingSource(
    private val context: Context,
    private val keyword: String
) :
    PagingSource<Int, NotificationRecordModel>() {

    private val thanox: ThanosManager = ThanosManager.from(context)
    private var todayTimeInMills: Long = DateUtils.getToadyStartTimeInMills()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NotificationRecordModel> {
        val current = params.key ?: 0
        val data = thanox.notificationManager.getAllNotificationRecordsByPageAndKeyword(
            current,
            params.loadSize,
            keyword
        )
        val next = if (data.isEmpty()) null else current + params.loadSize
        XLog.v("PageKeyedSubredditPagingSource.load current: $current next: $next loadSize: ${params.loadSize}")
        return Page(data = data.map { it.toModel() }, null, next)
    }

    override fun getRefreshKey(state: PagingState<Int, NotificationRecordModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            // This loads starting from previous page, but since PagingConfig.initialLoadSize spans
            // multiple pages, the initial load will still load items centered around
            // anchorPosition. This also prevents needing to immediately launch prepend due to
            // prefetchDistance.
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    private fun NotificationRecord.toModel(): NotificationRecordModel {
        XLog.v("toModel notificationRecord: %s", this)

        val appInfo = thanox.pkgManager.getAppInfo(this.pkgName) ?: uninstalledAppInfo(this)
        val isToday: Boolean = this.getWhen() >= todayTimeInMills
        val timeF =
            if (isToday) DateUtils.formatShortForMessageTime(this.getWhen()) else DateUtils.formatLongForMessageTime(
                this.getWhen()
            )
        return NotificationRecordModel(this, appInfo, timeF)
    }

    private fun uninstalledAppInfo(record: NotificationRecord): AppInfo {
        val dummy = AppInfo.dummy()
        dummy.appLabel =
            context.getString(R.string.module_notification_recorder_item_uninstalled_app)
        dummy.pkgName = record.pkgName
        return dummy
    }
}
