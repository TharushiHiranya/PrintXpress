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
    version = 3,
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

    // Ensures seed data is present on every database open if the catalog is empty.
    private class SeedCallback : RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            CoroutineScope(Dispatchers.IO).launch {
                INSTANCE?.let { database ->
                    // Only seed if categories table is empty to avoid duplicates.
                    if (database.categoryDao().getAll().isEmpty()) {
                        seedDatabase(database)
                    }
                }
            }
        }

        private suspend fun seedDatabase(database: PrintXpressDatabase) {
            val categoryDao = database.categoryDao()
            val productDao = database.productDao()
            val promotionDao = database.promotionDao()

            // Insert categories with fixed IDs for consistency during development.
            categoryDao.insert(Category(1, "Business cards", "Professional cards for networking and branding.", "badge"))
            categoryDao.insert(Category(2, "Posters", "Eye-catching posters for events and promotions.", "image"))
            categoryDao.insert(Category(3, "Banners", "Large format banners for indoor and outdoor use.", "panorama"))
            categoryDao.insert(Category(4, "Flyers", "Lightweight leaflets for quick distribution.", "article"))
            categoryDao.insert(Category(5, "Stickers", "Custom die-cut stickers in any shape.", "stars"))
            categoryDao.insert(Category(6, "Mugs", "Personalised mugs for gifts or branded merchandise.", "local_cafe"))
            categoryDao.insert(Category(7, "T-shirts", "Custom printed tees for individuals or teams.", "checkroom"))

            // Business cards products
            productDao.insert(Product(1, 1, "Standard business card", "Classic 85x55 mm card on 300 gsm stock.", 350.0, "Matte", "product_business_card", "85x55mm", "Matte,Glossy,Kraft"))
            productDao.insert(Product(2, 1, "Premium business card", "Thick 400 gsm card with soft-touch lamination.", 650.0, "Soft Touch", "product_business_card_premium", "85x55mm,90x50mm", "Soft Touch,Glossy Laminate"))

            // Posters products
            productDao.insert(Product(3, 2, "Event poster", "Vibrant A3 or A2 poster printed on 170 gsm gloss paper.", 850.0, "Glossy", "product_poster", "A4,A3,A2,A1", "Matte,Glossy"))
            productDao.insert(Product(4, 2, "Photo poster", "High-resolution photo print on premium satin paper.", 1200.0, "Satin", "product_photo_poster", "A4,A3,A2", "Satin,Glossy"))

            // Banners products
            productDao.insert(Product(5, 3, "Pull-up banner", "Retractable roll-up banner on 510 gsm material.", 3500.0, "Vinyl", "product_banner", "60x160cm,85x200cm", "Vinyl,Matte Vinyl"))
            productDao.insert(Product(6, 3, "Mesh banner", "Perforated vinyl banner for outdoor use.", 2400.0, "Mesh Vinyl", "product_mesh_banner", "100x200cm,150x300cm", "Mesh Vinyl"))

            // Flyers products
            productDao.insert(Product(7, 4, "A5 flyer", "Half-page flyer on 130 gsm silk paper.", 350.0, "Silk", "product_flyer", "A6,A5,A4", "Silk,Matte,Glossy"))
            productDao.insert(Product(8, 4, "DL flyer", "Slim DL size flyer that fits inside standard envelopes.", 280.0, "Silk", "product_dl_flyer", "DL,A5", "Silk,Matte"))

            // Stickers products
            productDao.insert(Product(9, 5, "Die-cut sticker", "Sticker cut to the exact shape of your design.", 90.0, "Vinyl", "product_sticker", "5x5cm,7x7cm,10x10cm", "Glossy Vinyl,Matte Vinyl"))
            productDao.insert(Product(10, 5, "Sheet of stickers", "Multiple stickers printed on one A4 sheet.", 450.0, "Vinyl", "product_sticker_sheet", "A4 sheet", "Glossy Vinyl,Matte Vinyl"))

            // Mugs products
            productDao.insert(Product(11, 6, "Classic mug", "11 oz ceramic mug with full-wrap print.", 950.0, "Ceramic", "product_mug", "11oz,15oz", "White Ceramic,Black Ceramic"))
            productDao.insert(Product(12, 6, "Colour-changing mug", "Reveals the print when filled with hot liquid.", 1450.0, "Ceramic", "product_magic_mug", "11oz", "Black Coating"))

            // T-shirts products
            productDao.insert(Product(13, 7, "Classic T-shirt", "100% cotton tee with front chest print.", 1650.0, "Cotton", "product_tshirt", "S,M,L,XL,XXL", "White,Black,Navy,Red"))
            productDao.insert(Product(14, 7, "Performance T-shirt", "Moisture-wicking polyester tee for sports.", 2100.0, "Polyester", "product_sport_tshirt", "S,M,L,XL,XXL", "White,Black,Navy"))

            // Sample promotions
            val now = System.currentTimeMillis()
            val day = 86_400_000L
            
            promotionDao.insert(Promotion(1, "20% off Business Cards", "Get 20% off on all business cards.", "CARDS20", now - day, now + 7 * day, 20, null, false, null, null, 1))
            promotionDao.insert(Promotion(2, "500 off Premium Cards", "Special discount on premium cards.", "PREMIUM500", now - day, now + 7 * day, null, 500.0, false, null, 2, null))
            promotionDao.insert(Promotion(3, "Free Delivery", "Free delivery on orders over LKR 5,000.", "FREEDEL", now - day, now + 30 * day, null, null, true, 5000.0, null, null))
        }
    }
}
