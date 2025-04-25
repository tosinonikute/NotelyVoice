import SwiftUI
import shared

struct NoteListScreenView: View {
	private var onFloatingButtonClicked: () -> Void
    private var onNoteClickedFun:(Int) -> Void
    private var refreshKey: UUID
    private static let selectedTabTitle = ""
    @State private var selectedTabTitleState = selectedTabTitle
    
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
            selectedTabTitle: selectedTabTitleState,
            onFloatingButtonClicked: {
                onFloatingButtonClicked()
            },
             onNoteClicked: { it in
                            onNoteClickedFun(it)
                        },
                         onFilterTabClicked: { it in
                                        selectedTabTitleState = it
                                    },
            onNoteDeleteClicked: { it in
                
            }
            ).id(refreshKey)
    }
}
