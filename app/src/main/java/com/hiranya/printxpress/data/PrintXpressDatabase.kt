package com.hiranya.printxpress.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hiranya.printxpress.data.dao.AddressDao
import com.hiranya.printxpress.data.dao.CategoryDao
import com.hiranya.printxpress.data.dao.NotificationDao
import com.hiranya.printxpress.data.dao.OrderDao
import com.hiranya.printxpress.data.dao.OrderItemDao
import com.hiranya.printxpress.data.dao.ProductDao
import com.hiranya.printxpress.data.dao.PromotionDao
import com.hiranya.printxpress.data.dao.SavedDesignDao
import com.hiranya.printxpress.data.dao.UserDao
import com.hiranya.printxpress.data.entity.Category
import com.hiranya.printxpress.data.entity.Notification
import com.hiranya.printxpress.data.entity.Product
import com.hiranya.printxpress.data.entity.Promotion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        com.hiranya.printxpress.data.entity.User::class,
        com.hiranya.printxpress.data.entity.Address::class,
        Category::class,
        Product::class,
        com.hiranya.printxpress.data.entity.Order::class,
        com.hiranya.printxpress.data.entity.OrderItem::class,
        com.hiranya.printxpress.data.entity.SavedDesign::class,
        Notification::class,
        Promotion::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PrintXpressDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun addressDao(): AddressDao
    abstract fun categoryDao(): CategoryDao
    abstract fun productDao(): ProductDao
    abstract fun orderDao(): OrderDao
    abstract fun orderItemDao(): OrderItemDao
    abstract fun savedDesignDao(): SavedDesignDao
    abstract fun notificationDao(): NotificationDao
    abstract fun promotionDao(): PromotionDao

    companion object {
        @Volatile
        private var INSTANCE: PrintXpressDatabase? = null

        fun getDatabase(context: Context): PrintXpressDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PrintXpressDatabase::class.java,
                    "printxpress_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(SeedCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    // Runs once when the database is first created to insert the starting catalog.
    private class SeedCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    seedDatabase(database)
                }
            }
        }

        private suspend fun seedDatabase(database: PrintXpressDatabase) {
            val categoryDao = database.categoryDao()
            val productDao = database.productDao()
            val promotionDao = database.promotionDao()

            // Insert the 7 product categories.
            val catBusinessCards = categoryDao.insert(Category(name = "Business cards", description = "Professional cards for networking and branding.", iconRef = "badge"))
            val catPosters = categoryDao.insert(Category(name = "Posters", description = "Eye-catching posters for events and promotions.", iconRef = "image"))
            val catBanners = categoryDao.insert(Category(name = "Banners", description = "Large format banners for indoor and outdoor use.", iconRef = "panorama"))
            val catFlyers = categoryDao.insert(Category(name = "Flyers", description = "Lightweight leaflets for quick distribution.", iconRef = "article"))
            val catStickers = categoryDao.insert(Category(name = "Stickers", description = "Custom die-cut stickers in any shape.", iconRef = "stars"))
            val catMugs = categoryDao.insert(Category(name = "Mugs", description = "Personalised mugs for gifts or branded merchandise.", iconRef = "local_cafe"))
            val catTshirts = categoryDao.insert(Category(name = "T-shirts", description = "Custom printed tees for individuals or teams.", iconRef = "checkroom"))

            // Business cards products
            productDao.insert(Product(categoryId = catBusinessCards, name = "Standard business card", description = "Classic 85x55 mm card on 300 gsm stock. Sharp text and vivid colours.", basePrice = 350.0, material = "Matte", imageRef = "product_business_card", sizeOptions = "85x55mm", materialOptions = "Matte,Glossy,Kraft"))
            productDao.insert(Product(categoryId = catBusinessCards, name = "Premium business card", description = "Thick 400 gsm card with soft-touch lamination for a luxury feel.", basePrice = 650.0, material = "Soft Touch", imageRef = "product_business_card_premium", sizeOptions = "85x55mm,90x50mm", materialOptions = "Soft Touch,Glossy Laminate"))

            // Posters products
            productDao.insert(Product(categoryId = catPosters, name = "Event poster", description = "Vibrant A3 or A2 poster printed on 170 gsm gloss paper.", basePrice = 850.0, material = "Glossy", imageRef = "product_poster", sizeOptions = "A4,A3,A2,A1", materialOptions = "Matte,Glossy"))
            productDao.insert(Product(categoryId = catPosters, name = "Photo poster", description = "High-resolution photo print on premium satin paper.", basePrice = 1200.0, material = "Satin", imageRef = "product_photo_poster", sizeOptions = "A4,A3,A2", materialOptions = "Satin,Glossy"))

            // Banners products
            productDao.insert(Product(categoryId = catBanners, name = "Pull-up banner", description = "Retractable roll-up banner on 510 gsm material, ideal for exhibitions.", basePrice = 3500.0, material = "Vinyl", imageRef = "product_banner", sizeOptions = "60x160cm,85x200cm", materialOptions = "Vinyl,Matte Vinyl"))
            productDao.insert(Product(categoryId = catBanners, name = "Mesh banner", description = "Perforated vinyl banner that lets wind pass through for outdoor use.", basePrice = 2400.0, material = "Mesh Vinyl", imageRef = "product_mesh_banner", sizeOptions = "100x200cm,150x300cm", materialOptions = "Mesh Vinyl"))

            // Flyers products
            productDao.insert(Product(categoryId = catFlyers, name = "A5 flyer", description = "Half-page flyer on 130 gsm silk paper. Great for promotions.", basePrice = 350.0, material = "Silk", imageRef = "product_flyer", sizeOptions = "A6,A5,A4", materialOptions = "Silk,Matte,Glossy"))
            productDao.insert(Product(categoryId = catFlyers, name = "DL flyer", description = "Slim DL size flyer that fits inside standard envelopes.", basePrice = 280.0, material = "Silk", imageRef = "product_dl_flyer", sizeOptions = "DL,A5", materialOptions = "Silk,Matte"))

            // Stickers products
            productDao.insert(Product(categoryId = catStickers, name = "Die-cut sticker", description = "Sticker cut to the exact shape of your design. Waterproof vinyl.", basePrice = 90.0, material = "Vinyl", imageRef = "product_sticker", sizeOptions = "5x5cm,7x7cm,10x10cm", materialOptions = "Glossy Vinyl,Matte Vinyl"))
            productDao.insert(Product(categoryId = catStickers, name = "Sheet of stickers", description = "Multiple stickers printed on one A4 sheet, cut into individual pieces.", basePrice = 450.0, material = "Vinyl", imageRef = "product_sticker_sheet", sizeOptions = "A4 sheet", materialOptions = "Glossy Vinyl,Matte Vinyl"))

            // Mugs products
            productDao.insert(Product(categoryId = catMugs, name = "Classic mug", description = "11 oz ceramic mug with full-wrap print. Dishwasher safe.", basePrice = 950.0, material = "Ceramic", imageRef = "product_mug", sizeOptions = "11oz,15oz", materialOptions = "White Ceramic,Black Ceramic"))
            productDao.insert(Product(categoryId = catMugs, name = "Colour-changing mug", description = "Reveals the print when filled with hot liquid. Great for gifts.", basePrice = 1450.0, material = "Ceramic", imageRef = "product_magic_mug", sizeOptions = "11oz", materialOptions = "Black Coating"))

            // T-shirts products
            productDao.insert(Product(categoryId = catTshirts, name = "Classic T-shirt", description = "100% cotton tee with front chest print. Available in all sizes.", basePrice = 1650.0, material = "Cotton", imageRef = "product_tshirt", sizeOptions = "S,M,L,XL,XXL", materialOptions = "White,Black,Navy,Red"))
            productDao.insert(Product(categoryId = catTshirts, name = "Performance T-shirt", description = "Moisture-wicking polyester tee for sports and active wear.", basePrice = 2100.0, material = "Polyester", imageRef = "product_sport_tshirt", sizeOptions = "S,M,L,XL,XXL", materialOptions = "White,Black,Navy"))

            // Two sample promotions: one active, one expired.
            val now = System.currentTimeMillis()
            val day = 86_400_000L
            promotionDao.insert(Promotion(title = "20% off business cards", description = "Premium quality at a lower price this week only.", discountPercent = 20, code = "CARDS20", validFrom = now - day, validTo = now + 6 * day))
            promotionDao.insert(Promotion(title = "Free delivery on orders over LKR 5k", description = "No delivery fee when your order total exceeds LKR 5,000.", discountPercent = 0, code = "AUTO", validFrom = now - day, validTo = now + 35 * day))
        }
    }
}
