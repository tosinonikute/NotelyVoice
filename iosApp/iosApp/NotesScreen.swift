//
//  NotesScreen.swift
//  iosApp
//

import SwiftUI
import shared

class NotesScreen: UIViewControllerRepresentable {
    private let module = AppModule()
    var onFloatingButtonClicked: () -> Void
    var onNoteClickedFun:(Int) -> Void
    var onNoteDeleteClickedFun:(Int) -> Void
    var uiState: NoteListUiState
    @ObservedObject var viewModel: IOSNoteListScreenViewModel
    
    init(
        onFloatingButtonClicked: @escaping () -> Void,
        onNoteClickedFun: @escaping (Int) -> Void,
        onNoteDeleteClickedFun: @escaping (Int) -> Void,
        uiState: NoteListUiState) {
        self.onFloatingButtonClicked = onFloatingButtonClicked
        self.onNoteClickedFun = onNoteClickedFun
        self.onNoteDeleteClickedFun = onNoteDeleteClickedFun
        self.uiState = uiState
            self.viewModel = IOSNoteListScreenViewModel(
                getAllNotesUseCase: module.getAllNotesUseCase,
                deleteNoteByIdUsecase: module.deleteNoteById,
                insertNoteUsecase: module.insertNote
            )
            viewModel.startObserving()
    }

    func makeUIViewController(context: Context) -> some UIViewController {
        AppKt.NoteListController(
            noteListUiState: viewModel.state,
            onFloatingActionButtonClicked: {
                viewModel.onEvent(event: NoteListEvent.InsertNote(title: "title -1", content: "content - 1"))
            },
            onNoteClicked: { it in
                // handle click event
            },
            onNoteDeleteClicked: { it in
                viewModel.onEvent(event: NoteListEvent.OnNoteDeleted(id: Int32(it)))
            }
        )
    }

    func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {
     
     }
}
