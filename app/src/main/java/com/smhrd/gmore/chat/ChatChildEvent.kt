import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.smhrd.gmore.chat.ChatAdapter
import com.smhrd.gmore.chat.ChatVO

class ChatChildEvent (var data : ArrayList<ChatVO>, var adapter: ChatAdapter) : ChildEventListener{
    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
        var temp : ChatVO? = snapshot.getValue(ChatVO::class.java)
        data.add(temp!!)
        adapter.notifyDataSetChanged()

    }

    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        // 데이터 변경 감지
        // ArrayList에 추가된 데이터 추가하고 Adapter 새로고침

    }

    override fun onChildRemoved(snapshot: DataSnapshot) {
        // 제거
    }

    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        // 옮겨짐
    }

    override fun onCancelled(error: DatabaseError) {
        // 뭔가 문제가 발생한걸 감지!
    }
}