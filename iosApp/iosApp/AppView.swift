//
//  AppView.swift
//  iosApp
//

import SwiftUI

struct AppView: View {
    @State private var navigateToSecondView = false
    @State private var selectedNoteIdState = -1
    @State private var refreshKey = UUID()
    
    var body: some View {
        NavigationView {
            VStack {
                NoteListScreenView(
                    onFloatingButtonClicked: {
                        navigateToSecondView = true
                    },
                    onNoteClicked: { it in
                        selectedNoteIdState = it
                        navigateToSecondView = true
                    
                    },
                    refreshKey: refreshKey
                )
                NavigationLink(
                    destination: SecondView(
                        navigateToSecondView: $navigateToSecondView,
                        selectedNoteId: selectedNoteIdState,
                        onNoteSaved: {
                            refreshKey = UUID()
                        }
                    ), isActive: $navigateToSecondView
                ) {
                    
                }
            }
        }
    }
}

struct SecondView: View {
    @Binding var navigateToSecondView: Bool
    @State var selectedNoteId: Int
    var onNoteSaved: () -> Void
    
    var body: some View {
        NoteDetailScreenController(
            onNoteSaveClicked: {
                navigateToSecondView = false
                onNoteSaved()
            }, noteId: String(selectedNoteId)
        )
    }
}

struct AppView_Previews: PreviewProvider {
    static var previews: some View {
        AppView()
    }
}
