export const refectoredProductData = (products) => {
    return products?.map(item => {
        return {
            variants: item.variants?.map(variant => {
                return {
                    available: variant.available,
                    title: variant.title,
                    compareAtPrice: variant.compareAtPrice,
                    id: variant.id,
                    price: variant.price,
                    priceV2: {amount: variant.priceV2.amount, currencyCode: variant.priceV2.currencyCode},
                    unitPriceMeasurement: {
                        measuredType: variant.unitPriceMeasurement.measuredType,
                        quantityUnit: variant.unitPriceMeasurement.quantityUnit,
                        quantityValue: variant.unitPriceMeasurement.quantityValue,
                        referenceUnit: variant.unitPriceMeasurement.referenceUnit,
                        referenceValue: variant.unitPriceMeasurement.referenceValue
                    },
                    unitPrice: variant.unitPrice,
                    image: variant.image.src
                };
            }),
            currentVariant: item.variants?.length ? {
                available: item.variants[0].available,
                title: item.variants[0].title,
                compareAtPrice: item.variants[0].compareAtPrice,
                id: item.variants[0].id,
                price: item.variants[0].price,
                priceV2: {amount: item.variants[0].priceV2.amount, currencyCode: item.variants[0].priceV2.currencyCode},
                unitPriceMeasurement: {
                    measuredType: item.variants[0].unitPriceMeasurement.measuredType,
                    quantityUnit: item.variants[0].unitPriceMeasurement.quantityUnit,
                    quantityValue: item.variants[0].unitPriceMeasurement.quantityValue,
                    referenceUnit: item.variants[0].unitPriceMeasurement.referenceUnit,
                    referenceValue: item.variants[0].unitPriceMeasurement.referenceValue
                },
                unitPrice: item.variants[0].unitPrice,
                image: item.variants[0].image.src
            } : {},
            updatedAt: item.updatedAt,
            quantityButton: false,
            title: item.title,
            productStatus: 'REMOVED',
            productType: item.productType,
            image: item.images,
            id: item.id,
            description: item.description,
            createdAt: item.createdAt,
            availableForSale: item.availableForSale
        };
    })
}
