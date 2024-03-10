//
//  ComposeView.swift
//  iosApp
//

import Foundation
import shared
import SwiftUI

struct NoteListScreen: UIViewControllerRepresentable {
    private var onFloatingButtonClicked: () -> Void
    private var onNoteClickedFun:(Int) -> Void
    private var onNoteDeleteClickedFun:(Int) -> Void
    init(
        onFloatingButtonClicked: @escaping () -> Void,
        onNoteClicked:@escaping (Int) -> Void,
        onNoteDeleteClicked:@escaping (Int) -> Void
    ) {
        self.onFloatingButtonClicked = onFloatingButtonClicked
        self.onNoteClickedFun = onNoteClicked
        self.onNoteDeleteClickedFun = onNoteDeleteClicked
    }
  
   func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {
    
    }
    
    func makeUIViewController(context: Context) -> some UIViewController {
        NoteListControllerKt.NoteListController(
            onFloatingActionButtonClicked: {
                onFloatingButtonClicked()
            },
            onNoteClicked: { it in
                onNoteClickedFun(Int(it))
            }
        )
    }
}
