// domain/usecase/userprofile/GetCommonSymptomsUseCase.kt
package com.kcguran.menstrualtracking.domain.usecase.userprofile

import javax.inject.Inject

class GetCommonSymptomsUseCase @Inject constructor() {
    /**
     * Regl dönemi için yaygın semptomların listesini döndürür.
     *
     * @return Yaygın semptomların listesi
     */
    operator fun invoke(): List<String> {
        return listOf(
            "Karın krampları",
            "Bel ağrısı",
            "Baş ağrısı",
            "Yorgunluk",
            "Göğüslerde hassasiyet",
            "Duygusal değişimler",
            "Şişkinlik",
            "İştah değişimleri",
            "Akne",
            "Uykusuzluk",
            "Mide bulantısı",
            "Baş dönmesi",
            "Konsantrasyon eksikliği",
            "İshal",
            "Kabızlık",
            "Alt karın ağrısı",
            "Sıcak basması",
            "Halsizlik",
            "Gerginlik",
            "Sinirlilik"
        )
    }
}