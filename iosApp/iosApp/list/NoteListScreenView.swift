import SwiftUI
import shared

struct NoteListScreenView: View {
	private var onFloatingButtonClicked: () -> Void
    private var onNoteClickedFun:(Int) -> Void
    private var refreshKey: UUID
    
    init(
        onFloatingButtonClicked: @escaping () -> Void,
        onNoteClicked:@escaping (Int) -> Void,
        refreshKey: UUID
    ) {
        self.onFloatingButtonClicked = onFloatingButtonClicked
        self.onNoteClickedFun = onNoteClicked
        self.refreshKey = refreshKey
    }
    
	var body: some View {
            NoteListScreen(
            onFloatingButtonClicked: {
                onFloatingButtonClicked()
            },
            onNoteClicked: { it in
                onNoteClickedFun(it)
            },
            onNoteDeleteClicked: { it in
                
            }
            ).id(refreshKey)
    }
}
