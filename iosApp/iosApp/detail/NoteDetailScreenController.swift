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
    
    init(onNoteSaveClicked: @escaping () -> Void, noteId: String) {
        self.onNoteSaveClicked = onNoteSaveClicked
        self.noteId = noteId
    }
    func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {
     
     }
     
     func makeUIViewController(context: Context) -> some UIViewController {
         NoteListControllerKt.NoteDetailController(
            noteId: noteId,
            onSaveClicked: {
                onNoteSaveClicked()
            }
         )
     }
}
