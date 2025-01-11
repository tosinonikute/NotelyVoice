//
//  NoteDetailScreenController.swift
//  iosApp
//

import Foundation
import shared
import SwiftUI


struct NoteDetailScreenController : UIViewControllerRepresentable  {
    private var onNoteSaveClicked:() -> Void
    private var noteId: String
    private var onNavigateBack:() -> Void
    
    init(onNoteSaveClicked: @escaping () -> Void, noteId: String, onNavigateBack: @escaping () -> Void) {
        self.onNoteSaveClicked = onNoteSaveClicked
        self.noteId = noteId
        self.onNavigateBack = onNavigateBack
    }
    
    func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {
     
    }

    func makeUIViewController(context: Context) -> some UIViewController {
         NoteListControllerKt.NoteDetailController(
            noteId: noteId,
            onSaveClicked: {
                onNoteSaveClicked()
            },
            onNavigateBack: {
                onNavigateBack()
            }
         )
     }
}
