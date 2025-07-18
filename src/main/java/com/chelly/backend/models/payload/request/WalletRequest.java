package com.chelly.backend.models.payload.request;

import com.chelly.backend.models.annotations.EnumValue;
import com.chelly.backend.models.enums.WalletName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WalletRequest {
    @NotBlank(message = "Nama wallet tidak boleh kosong")
    @EnumValue(enumClass = WalletName.class, message = "Nama wallet tidak valid. Pilihan: Dana, GoPay, OVO, ShopeePay, DANA, LinkAja")
    private String walletName;

    @NotBlank(message = "Nomor rekening tidak boleh kosong")
    @Size(min = 5, max = 20, message = "Nomor rekening harus antara 5 hingga 20 karakter")
    private String accountNumber;

    @NotBlank(message = "Nama pemilik rekening tidak boleh kosong")
    @Size(max = 150, message = "Nama pemilik rekening tidak boleh lebih dari 150 karakter")
    private String accountHolderName;

    @NotNull(message = "Status 'default' tidak boleh kosong")
    private Boolean isDefault = false;
}
