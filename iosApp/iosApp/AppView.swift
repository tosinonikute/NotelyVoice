//
//  AppView.swift
//  iosApp
//

import SwiftUI

struct AppView: View {
    private static let defaultNoteId = -1
    @State private var navigateToSecondView = false
    @State private var selectedNoteIdState = defaultNoteId
    @State private var refreshKey = UUID()
    
    var body: some View {
        NavigationView {
            VStack {
                NoteListScreenView(
                    onFloatingButtonClicked: {
                        navigateToSecondView = true
                        selectedNoteIdState = AppView.defaultNoteId
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
        }.navigationViewStyle(.stack)
    }
}

struct SecondView: View {
    @Binding var navigateToSecondView: Bool
    @State var selectedNoteId: Int
    var onNoteSaved: () -> Void
    
    var body: some View {
        NoteDetailScreenController(
            onNoteSaveClicked: {
                
            },

            noteId: String(selectedNoteId),
            onNavigateBack: {
                navigateToSecondView = false
                onNoteSaved()
            }
        )
        .ignoresSafeArea(.keyboard)
        .navigationBarHidden(true) // set to false to show native back button

}
}

struct AppView_Previews: PreviewProvider {
    static var previews: some View {
        AppView()
    }
}
