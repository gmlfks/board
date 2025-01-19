// /js/comment.js 파일 내에 추가
$('#btn-comment-save').click(function() {
    const postId = $('#postsId').val();  // 게시글 ID
    const content = $('#comment-content').val();  // 댓글 내용

    if (content.trim() === "") {
        alert("댓글 내용을 입력해주세요.");
        return;
    }

    const requestData = {
        comments : content  // 댓글 내용을 포함
    };

    // 댓글 저장 요청
    fetch(`/api/post/${postId}/comment`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestData)
    })
    .then(response => {
        if (response.ok) {
            response.json().then(commentId => {
                alert("댓글이 작성되었습니다.");
                location.reload("/post/read/${postId}");  // 댓글 작성 후 페이지 새로고침 (댓글 목록 갱신)
            });
        } else {
            alert("댓글 작성 실패");
        }
    })
    .catch(error => {
        alert("댓글 작성 중 오류 발생");
        console.error("댓글 작성 요청 실패:", error);
    });
});

// 댓글 삭제
function deleteComment(commentId, postId) {
    if (confirm("정말 삭제하시겠습니까?")) {
        fetch(`/api/post/${postId}/comment/${commentId}`, {
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json' },
        })
        .then(response => {
            if (response.ok) {
                alert("댓글이 삭제되었습니다.");
                location.reload(); // 댓글 삭제 후 페이지 새로고침 (댓글 목록 갱신)
            } else {
                alert("삭제 실패");
            }
        })
        .catch(error => {
            alert("삭제 중 오류 발생");
            console.error("댓글 삭제 요청 실패:", error);
        });
    }
}

// 댓글 수정 (예시: 수정하는 폼을 띄우는 방식)
function editComment(commentId, postId) {
    const newContent = prompt("수정할 댓글 내용을 입력하세요:");

    if (newContent && newContent.trim() !== "") {
        const requestData = { comments: newContent };

        fetch(`/api/post/${postId}/comment/${commentId}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(requestData)
        })
        .then(response => {
            if (response.ok) {
                alert("댓글이 수정되었습니다.");
                location.reload("/post/read/${postId}"); // 수정 후 페이지 새로고침 (댓글 목록 갱신)
            } else {
                alert("수정 실패");
            }
        })
        .catch(error => {
            alert("수정 중 오류 발생");
            console.error("댓글 수정 요청 실패:", error);
        });
    } else {
        alert("댓글 내용은 비어 있을 수 없습니다.");
    }
}